<template>
  <div align='middle'>
    <b-button @click='signOut'>Sign out
      <b-icon icon='box-arrow-right'></b-icon>
    </b-button>
    <b-col>
      <b-card id='account-edit-card' align='left'>
        <template #header>
          Changing account settings
        </template>
        <b-form-group
            label="Name"
        >
          <b-form-input
              v-model="user.firstName"
              required
              placeholder="First name"
          ></b-form-input>
          <br>
          <b-form-input
              v-model="user.lastName"
              required
              placeholder="Last name"
          ></b-form-input>
        </b-form-group>
        <b-form-group v-if='user.tutorAccount'
                      label="Info"
        >
          <b-form-input
              v-model="user.info"
              required
              placeholder="First name"
          ></b-form-input>
        </b-form-group>
        <b-form-group
            v-if='user.tutorAccount'
            prepend="£"
            label="Your hourly rate"
        >
          <b-form-input
              v-model="user.rate"
              type="number"
              required
              placeholder="Hourly rate"
          ></b-form-input>
        </b-form-group>
        <b-form-group
            label="Email"
        >
          <b-form-input
              v-model="user.email"
              type="email"
              required
              placeholder="Enter email"
          ></b-form-input>
        </b-form-group>
        <b-form-group
            label="Phone"
        >
          <b-form-input
              v-model="user.phone"
              required
              placeholder="Enter phone number"
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Change password">
          <p class='text-muted'>
            (minimum 8 characters and one capital letter)
          </p>
          <b-form-input
              type='password'
              v-model="user.password1"
              required
              placeholder="Enter password"
          ></b-form-input>
          <br>
          <b-form-input
              type='password'
              v-model="user.password2"
              required
              placeholder="Repeat password"
          ></b-form-input>
        </b-form-group>
        <b-button block @click='submit' variant="primary">Save</b-button>
        <br>
        <b-row align-v='center'>
          <b-col sm='12' md='8' align='middle'>
            <b-form-checkbox style='margin-bottom: 0.25rem' button button-variant='outline-primary'
                             v-model='deactivateEnabled'>
              Deactivate my account
            </b-form-checkbox>
          </b-col>
          <b-col sm='12' md='4' align='middle'>
            <b-button style='margin-bottom: 0.25rem' :disabled='!deactivateEnabled'
                      @click='deactivate'
                      variant="danger">
              Deactivate
            </b-button>
          </b-col>
        </b-row>
      </b-card>
    </b-col>
  </div>
</template>

<script>

export default {
  layout: 'index',
  name: 'Account',
  props: [
    'user'
  ],
  data() {
    return {
      deactivateEnabled: false
    }
  },
  mounted() {

  },
  methods: {
    submit() {
      if (this.user.password1 || this.user.password2) {
        if (this.user.password1 != this.user.password2) {
          this.$nuxt.$emit("error", "passwords_not_matching")
        }
      }
      if (this.user.tutorAccount) {
        this.$services.service.editTutorAccount(this.user.firstName, this.user.lastName, this.user.email,
            this.user.phone, this.user.rate, this.user.info, this.user.password1).then((res) => {
          this.$nuxt.$emit("success", "successfully_saved_changes")
        }).catch((error) => {
          // check if we have a string response data. these are usually custom defined on server side
          if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
            this.$nuxt.$emit("error", error.response.data)
          // check if we have an error message
          else if (typeof error.message === "string" || error.message instanceof String) this.$nuxt.$emit("error", error.message)
          // no error message, this was unexpected
          else this.$nuxt.$emit("error", "unexpected_error")
        })
      } else {
        this.$services.service.editStudentAccount(this.user.firstName, this.user.lastName, this.user.email,
            this.user.phone, this.user.password1).then((res) => {
          this.$nuxt.$emit("success", "successfully_saved_changes")
        }).catch((error) => {
          // check if we have a string response data. these are usually custom defined on server side
          if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
            this.$nuxt.$emit("error", error.response.data)
          // check if we have an error message
          else if (typeof error.message === "string" || error.message instanceof String) this.$nuxt.$emit("error", error.message)
          // no error message, this was unexpected
          else this.$nuxt.$emit("error", "unexpected_error")
        })
      }

    },
    deactivate() {
      this.$services.service.deactivate(this.user.id).then((res) => {
        this.$nuxt.$emit("success", "succesfully_deactivated")
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
    signOut() {
      this.$cookies.remove('user')
      this.$services.service.signOut();
      this.$nuxt.$emit("event", {name: "signout"})
      this.$nuxt.$router.push("/login")
    }
  }
}
</script>

<style>
#account-edit-card {
  max-width: 28rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
}
</style>
