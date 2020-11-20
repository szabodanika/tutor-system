<template>
  <div class="container" align='middle'>
    <b-col>
      <b-card id='password-reset-card' align='left'>
        <h5>1. Request password reset</h5>
        <b-form-group
            label="Email"
        >
          <b-form-input
              v-model="reset.email"
              type="email"
              required
              placeholder="Enter email"
          ></b-form-input>
        </b-form-group>
        <b-button block @click='requestCode' variant="primary">Request password reset</b-button>
        <br>
        <p class='text-muted'>
          If there is an account with the entered email address, you will receive a reset code in a few minutes that
          will remain active for 1 hour.
        </p>
        <br>
        <h5>2. Reset password using reset code</h5>
        <b-form-group label="Reset code">
          <p class='text-muted'>
            Enter the reset code we sent you to your email address
          </p>
          <b-form-input
              type='number'
              v-model="reset.code"
              required
              placeholder="Reset code"
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Your new password">
          <p class='text-muted'>
            (minimum 8 characters and one capital letter)
          </p>
          <b-form-input
              type='password'
              v-model="reset.password1"
              required
              placeholder="Enter password"
          ></b-form-input>
          <br>
          <b-form-input
              type='password'
              v-model="reset.password2"
              required
              placeholder="Repeat password"
          ></b-form-input>
        </b-form-group>
        <b-button block @click='resetPassword' variant="primary">Set new password</b-button>
      </b-card>
    </b-col>
  </div>
</template>

<script>

export default {
  layout: 'index',
  name: 'PasswordReset',
  props: [
  ],
  data() {
    return {
      reset: {},
    }
  },
  mounted() {
  },
  methods: {
    requestCode() {
      this.$services.service.requestResetCode(this.reset.email).then((res) => {
        this.$nuxt.$emit("success", "successfully_requested_reset")
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
    resetPassword() {
      if(this.reset.password1 || this.reset._password2){
        if(this.reset.password1 != this.reset.password2) {
          this.$nuxt.$emit("error", "passwords_not_matching")
          return
        }
      }

      this.$services.service.resetPassword(this.reset.code, this.reset.password1).then((res) => {
        this.$nuxt.$emit("success", "successfully_reset_password")
        this.$nuxt.$router.push("/login")
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


<style scoped>
  #password-reset-card {
    max-width: 28rem;
  }
</style>