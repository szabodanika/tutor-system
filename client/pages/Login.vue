<template>
  <div id="login-container" align='middle' @keypress.enter='submitLogin'>
    <b-row v-if='!user'>
      <b-col>
        <br>
        <br>
        <b-button @click='$nuxt.$router.push("/signup/student")' variant="primary">Join your tutor</b-button>
        <b-button @click='$nuxt.$router.push("/signup/activation")' variant="primary">Activate account</b-button>
        <br>
        <br>
        <b-card id='login-card' align='left'>
          <template #header>
            Log in here
          </template>
          <b-form-group
              label="Email"
          >
            <b-form-input
                v-model="login.email"
                type="email"
                required
                placeholder="Enter email"
            ></b-form-input>
          </b-form-group>
          <b-form-group label="Password">
            <b-form-input
                type='password'
                v-model="login.password"
                required
                placeholder="Enter password"
            ></b-form-input>
          </b-form-group>
          <b-form-group>
            <b-form-checkbox v-model='login.rememberMe'>Keep me logged in for an hour</b-form-checkbox>
          </b-form-group>
          <b-button block @click='submitLogin' variant="primary">Log in
            <b-icon icon='key'></b-icon>
          </b-button>
        </b-card>
        <nuxt-link to='#' align='middle'>
          <b-link>Forgot password?</b-link>
        </nuxt-link>
        <br>
        <NuxtLink to='/signup/tutor' align='middle'>
          Create tutor account
        </NuxtLink>
        <br>
        <br>

      </b-col>
    </b-row>
  </div>
</template>

<script>

export default {
  layout: 'index',
  name: 'Login',
  props: [
    'user'
  ],
  data() {
    return {
      login: {
        email: null,
        password: null,
        rememberMe: true
      },
      tutorSignup: {
        email: 'testtutor2020@email.com',
      }
    }
  },
  mounted() {
    let email = this.$cookies.get("email")
    if (email) {
      this.login.email = email
    }
  },
  methods: {
    submitLogin() {
      if (!this.login.email || !this.login.password) {
        this.$nuxt.$emit("warning",
            "Please enter your email and password to continue.")
        return
      }
      this.$services.service.login(this.login.email, this.login.password).then((res) => {
        if (this.login.rememberMe) {
          this.$cookies.set('user', res.id, '1h')
        }
        this.$cookies.set('email', this.login.email, '14d')
        this.$nuxt.$emit('event', {
          name: 'login',
          value: res
        })
        this.$nuxt.$emit("success", "successful_login")
        this.$nuxt.$router.push("/home")
      }).catch((error) => {
        // check if we have a string response data. these are usually custom defined on server side
        if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
          this.$nuxt.$emit("error", error.response.data)
        // check if we have an error message
        else if (typeof error.message === "string" || error.message instanceof String) this.$nuxt.$emit("error", error.message)
        // no error message, this was unexpected
        else this.$nuxt.$emit("error", "unexpected_error")
      })
    },
  }
}
</script>

<style>
#login-card {
  max-width: 25rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
