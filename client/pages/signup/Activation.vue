<template>
  <b-overlay>
    <div class="container" align='middle'>
      <b-col>
        <b-card id='tutor-signup-card' align='left'>
          <template #header>
            Activate student account
          </template>
          <b-form-group
              label="Enter the 6 digit tutor code"
          >
            <b-form-input
                v-model="signup.tutorcode"
                type="number"
                required
                placeholder="6 digit tutor code"
            ></b-form-input>
          </b-form-group>
          <b-form-group
              label="Enter the 6 digit activation code"
          >
            <b-form-input
                v-model="signup.activationcode"
                type="number"
                required
                placeholder="6 digit tutor code"
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
      </b-col>
    </div>
  </b-overlay>
</template>

<script>

export default {
  layout: 'index',
  name: 'Activation',
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
      this.$services.service.activateStudent(this.signup.tutorcode, this.signup.activationcode, this.signup.email,
          this.signup.password1).then((res) => {
        this.$nuxt.$emit("success", "successfully_activated")
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
#tutor-signup-card {
  max-width: 28rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
