<template>
  <div id='root'>
    <b-navbar id='navigation' toggleable="lg" variant="light" type="light" sticky>
      <b-navbar-brand href="#">
        oktatutor
      </b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <nuxt-link v-if='user' to="/home" class='nav-link'>
            Home
          </nuxt-link>
          <nuxt-link v-else to="/" class='nav-link'>
            Home
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user'>
          <nuxt-link to="/lessons" class='nav-link'>
            Lessons
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user'>
          <nuxt-link to="/students" class='nav-link'>
            Students
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user'>
          <nuxt-link to="/payments" class='nav-link'>
            Payments
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user'>
          <nuxt-link to="/account" class='nav-link'>
            {{ user.firstName }} {{ user.lastName }}
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-else>
          <nuxt-link to="/login" class='nav-link'>
            Login
          </nuxt-link>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
    <Login v-if='restrictedPages.includes($nuxt.$route.path) && !user' :user='user'
           class='content'/>
    <NuxtChild v-else :user='user' class='content'/>

  </div>
</template>

<script>
import Vue from 'vue'
import VueRouter from 'vue-router'
import Lessons from "@/pages/Lessons";
import Login from "@/pages/Login";
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import VueCookies from 'vue-cookies'

Vue.use(VueCookies)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)

Vue.use(VueRouter)
export default {
  name: 'Index',
  components: {Login, Lessons},
  data() {
    return {
      user: null,
      restrictedPages: [
        "/account",
        "/lessons",
        "/newlesson",
        "/newpayment",
        "/payments",
        "/newstudent",
        "/students"]
    }
  },
  watch: {},
  mounted() {
    let user = this.$cookies.get('user')
    if(user) {
      this.user = this.getUser(user)
    }
    this.$nuxt.$on("event", (event) => {
      if (event.name == "login") {
        this.user = event.value
      } else if (event.name == "signout") {
        this.user = null
      }
    })
    this.$nuxt.$on("error", this.handleError)
    this.$nuxt.$on("info", this.handleInfo)
    this.$nuxt.$on("warning", this.handleWarning)
    this.$nuxt.$on("success", this.handleSuccess)
  },
  methods: {
    handleError(msg) {
      let messages = {
        'unexpected_error': 'An unexpected error has occured.',
        'invalid_session': 'Invalid user session. Please log in again.',
        'invalid_credentials': 'Invalid credentials',
        'not_logged_in': 'Please log in to continue',
        'login_closed': 'Login service has been temporarily closed.',
        'signup_closed': 'Registration service has been temporarily closed.',
        'api_closed': 'Data service has been temporarily closed.'
      }
      if (msg == 'not_logged_in') this.$nuxt.$router.push("/login")
      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Error",
        toaster: 'b-toaster-top-right',
        variant: 'danger'
      })
    },
    handleInfo(msg) {
      let messages = {}

      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Notification",
        toaster: 'b-toaster-top-right',
        variant: 'info'
      })
    },
    handleWarning(msg) {
      let messages = {}

      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Warning",
        toaster: 'b-toaster-top-right',
        variant: 'warning'
      })
    },
    handleSuccess(msg) {
      let messages = {
        'successful_login': `Welcome back${this.user?`, ${this.user.firstName}`:``}`,
        'successfully_registered': 'Registration successful. You can log in now.',
        'successfully_created_virtual_student': 'Student successfully added.',
        'successfully_activated': 'Successfully activated account.'
      }

      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Success",
        toaster: 'b-toaster-top-right',
        variant: 'success'
      })
    },
    getUser(user){
      this.$services.service.getUserById(user).then((res) => {
        console.log(res)
        return res
      }).catch((error) => {
        // check if we have a string response data. these are usually custom defined on server side
        if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
          this.handleError(error.response.data)
        // check if we have an error message
        else if (typeof error.message === "string" || error.message instanceof String) this.handleError(error.message)
        // no error message, this was unexpected
        else this.handleError("unexpected_error")
      })
    }
  }
};
</script>

<style scoped>

#root {
  height: 100vh;
}

#navigation {
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.content {
  padding-top: 1rem;
  padding-left: 1rem;
  padding-right: 1rem;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  min-height: 100%
}

.nav-link {
  margin: 0;
  color: white;
}
</style>
