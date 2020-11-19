<template>
  <b-overlay>
    <div class="container" align='middle'>
      <b-col>
        <b-card id='student-signup-card' align='left'>
          <template #header>
            Join your tutor
          </template>
          <b-form-group
              label="Enter the 6 digit code your tutor gave you"
          >
            <b-form-input
                v-model="signup.tutorcode"
                type="number"
                required
                placeholder="6 digit tutor code"
            ></b-form-input>
          </b-form-group>
          <b-form-group
              label="Name"
          >
            <b-form-input
                v-model="signup.firstname"
                required
                placeholder="First name"
            ></b-form-input>
            <br>
            <b-form-input
                v-model="signup.lastname"
                required
                placeholder="Last name"
            ></b-form-input>
          </b-form-group>
          <b-form-group
              label="Email"
          >
            <b-form-input
                v-model="signup.email"
                type="email"
                required
                placeholder="Enter email"
            ></b-form-input>
          </b-form-group>
          <b-form-group label="Password">
            <p class='text-muted'>
              (minimum 8 characters and one capital letter)
            </p>
            <b-form-input
                type='password'
                v-model="signup.password1"
                required
                placeholder="Enter password"
            ></b-form-input>
            <br>
            <b-form-input
                type='password'
                v-model="signup.password2"
                required
                placeholder="Repeat password"
            ></b-form-input>
          </b-form-group>
          <b-button block @click='submit' variant="primary">Sign up</b-button>
        </b-card>
        <NuxtLink to='/signup/activation' align='middle'>
          Activate existing account
        </NuxtLink>
      </b-col>
    </div>
  </b-overlay>
</template>

<script>

export default {
  layout: 'index',
  name: 'SignupStudent',
  props: [],
  data() {
    return {
      signup: {},
    }
  },
  mounted() {

  },
  methods: {
    submit() {
      if(this.signup.password1 || this.signup._password2){
        if(this.signup.password1 != this.signup.password2) {
          this.$nuxt.$emit("error", "passwords_not_matching")
          return
        }
      }
      this.$services.service.register(this.signup.email, this.signup.password1, this.signup.firstname,
          this.signup.lastname, this.signup.tutorcode, -1).then((res) => {
        this.$nuxt.$emit('event', {
          name: 'login',
          value: res
        })
        this.$nuxt.$emit("success", "succesfully_registered")
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
#student-signup-card {
  max-width: 28rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
