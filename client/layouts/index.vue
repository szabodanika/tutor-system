<template>
  <div id='root'>
    <b-navbar id='navigation' toggleable="lg" variant="light" type="light" sticky align='center'>
      <b-navbar-brand href="/">
        oktatutor
      </b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <nuxt-link to="/" class='nav-link'>
            Home
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user && user.tutorAccount'>
          <nuxt-link to="/lessons" class='nav-link'>
            Lessons
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user && user.tutorAccount'>
          <nuxt-link to="/students" class='nav-link'>
            Students
          </nuxt-link>
        </b-navbar-nav>
        <b-navbar-nav v-if='user && user.tutorAccount'>
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
    <b-overlay :show="isLoading" class='content' :rounded='true'
               variant='transparent' spinner-type='grow'  :opacity="1.0"  blur="1rem">
      <span v-if='!isLoading'>
        <NuxtChild v-if='(user || !restrictedPages.includes($nuxt.$route.path)) &&
        (user || notrestrictedPages.includes($nuxt.$route.path))' :user='user' class='content'/>
        <Login v-else class='content'/>
      </span>
    </b-overlay>

  </div>
</template>

<script>
import Vue from 'vue'
import VueRouter from 'vue-router'
import Lessons from "@/pages/Lessons";
import Login from "@/pages/Login";
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'
import VueCookies from 'vue-cookies'

Vue.use(VueCookies)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)

Vue.use(VueRouter)
export default {
  name: 'Account',
  components: {Login, Lessons},
  data() {
    return {
      user: null,
      isLoading: true,
      restrictedPages: [
        "/account",
        "/lessons",
        "/newlesson",
        "/newpayment",
        "/payments",
        "/newstudent",
        "/students"],
      notrestrictedPages: [
        "/",
        "/signup",
        "/signup/activation",
        "/signup/student",
        "/signup/tutor",
        "/login",
        "/passwordreset"]
    }
  },
  watch: {},
  mounted() {
    let user = this.$cookies.get('user')
    if (user) this.getUser(user)
    else this.isLoading = false

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
        'session_expired': 'Your login session has expired. Please log in to continue.',
        'invalid_credentials': 'Invalid credentials',
        'not_logged_in': 'Please log in to continue',
        'login_closed': 'Login service has been temporarily closed.',
        'signup_closed': 'Registration service has been temporarily closed.',
        'api_closed': 'Data service has been temporarily closed.',
        'passwords_not_matching': 'Your passwords are not matching.',
        'account_disabled': 'This account has been disabled.',
        'password_reset_failed': 'Failed to reset password.',
        'incorrect_name': 'Your name cannot contain any special characters and you have to enter a first name',
        'incorrect_email': 'Invalid email address',
        'incorrect_phone': 'Invalid phone number',
        'incorrect_password':
            'Your password has to contain at least a capital letter and a number and has to at least 8 characters'
      }
      if (msg == 'not_logged_in') {
        this.$nuxt.$router.push("/login")
        this.user = null
      }
      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Error",
        toaster: 'b-toaster-top-center',
        variant: 'danger'
      })
    },
    handleInfo(msg) {
      let messages = {}

      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Notification",
        toaster: 'b-toaster-top-center',
        variant: 'info'
      })
    },
    handleWarning(msg) {
      let messages = {}

      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Warning",
        toaster: 'b-toaster-top-center',
        variant: 'warning'
      })
    },
    handleSuccess(msg) {
      let messages = {
        'successful_login': `Welcome back${this.user ? `, ${this.user.firstName}` : ``}`,
        'successfully_registered': 'Registration successful. You can log in now.',
        'successfully_created_virtual_student': 'Student successfully added.',
        'successfully_activated': 'Successfully activated account.',
        'successfully_saved_lesson': 'Lesson saved successfully.',
        'successfully_locked_lesson': 'Lesson locked successfully.',
        'successfully_unlocked_lesson': 'Lesson unlocked successfully.',
        'successfully_saved_student': 'Student saved successfully.',
        'successfully_saved_payment': 'Payment saved successfully.',
        'successfully_saved_changes': 'Changes saved successfully.',
        'successfully_requested_reset': 'Requested password reset code',
        'successfully_reset_password': 'You have successfully changed your password',
        'successfully_deleted_lesson': 'Lesson successfully removed'
      }

      this.$bvToast.toast(msg in messages ? messages[msg] : msg, {
        title: "Success",
        toaster: 'b-toaster-top-center',
        variant: 'success'
      })
    },
    getUser(user) {
      this.isLoading = true
      this.$services.service.getUserById(user).then((res) => {
        this.user = res
      }).catch((error) => {
        // check if we have a string response data. these are usually custom defined on server side
        if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
          this.handleError(error.response.data)
        // check if we have an error message
        else if (typeof error.message === "string" || error.message instanceof String) this.handleError(error.message)
        // no error message, this was unexpected
        else this.handleError("unexpected_error")

        this.$cookies.remove('user')
      }).finally(() => {
        this.isLoading = false
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
  /*max-width: 1400px;*/
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 0 1em rgba(0, 0, 0, 0.3);
  background-color: #ffffff;
  text-align: center;
}

.content {
  padding-top: 1rem;
  /*padding-left: 1rem;*/
  /*padding-right: 1rem;*/
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  padding-bottom: 4rem;
  min-height: 50vh;
}

.nav-link {
  margin: 0;
  color: white;
}
</style>
