<template>
  <b-overlay>
    <div class="container" align='middle'>
      <b-col>
        <b-card id='new-virtual-student-card' align='left'>
          <template #header>
            Create student
          </template>
          <p>
            You can add a student to the database without them creating an account.
            The user account created here can be claimed by a generated password that you will be able to access later.
          </p>
          <b-form-group
              label="Name"
          >
            <b-form-input
                v-model="signup.firstname"
                type="name"
                required
                placeholder="First name"
            ></b-form-input>
            <br>
            <b-form-input
                v-model="signup.lastname"
                type="name"
                required
                placeholder="Last name"
            ></b-form-input>
          </b-form-group>
          <b-input-group
              prepend="£"
              label="Student's hourly rate (this can be changed later)"
          >
            <b-form-input
                v-model="signup.rate"
                type="number"
                required
                placeholder="Hourly rate"
            ></b-form-input>
          </b-input-group>
          <br>
          <b-button block @click='submit' variant="primary">Create student</b-button>
        </b-card>
      </b-col>
    </div>
  </b-overlay>
</template>

<script>

export default {
  layout: 'index',
  name: 'NewPayment',
  props: [
    'user'
  ],
  data() {
    return {
      signup: {},
    }
  },
  mounted() {
  },
  methods: {
    submit() {
      this.$services.service.registerVirtualStudent(this.signup.firstname, this.signup.lastname, this.signup.rate).then((res) => {
        this.$nuxt.$emit("success", "successfully_created_virtual_student")
        this.$nuxt.$router.push("/students")
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
#new-virtual-student-card {
  max-width: 25rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
