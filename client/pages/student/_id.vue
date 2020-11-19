<template>
  <b-overlay :show="isLoading" rounded="sm" :rounded='true' variant='transparent'
             spinner-type='grow' :opacity="1.0" blur="1rem" id='overlay'>
    <b-card id='new-lesson-card' align='left'>
      <template #header>
        Manage Student
      </template>
      <h3>
        {{ student.firstName }}
        {{ student.lastName }}
      </h3>
      <b-form-group label="Student's hourly rate ">
        <b-input-group prepend="£">
          <b-form-input
              :disabled='student.disabled'
              v-model="student.rate"
              type="number"
              required
              placeholder="Hourly rate"
          ></b-form-input>
        </b-input-group>
      </b-form-group>
      <br>
      <b-button v-if='!student.disabled' :disabled='student.disabled' block @click='submit' variant="primary">Save
      </b-button>
      <b-button :disabled='student.disabled' block @click='disable'
                variant="danger">{{student.disabled?"Disabled":"Disable"}}</b-button>
    </b-card>
  </b-overlay>
</template>

<script>
export default {
  components: {},
  layout: 'index',
  name: '_',
  props: [
    'user'
  ],
  data() {
    return {
      student: {},
      isLoading: true
    }
  },
  watch: {
    id: function (val) {
      if (val) this.getStudent()
    }
  },
  asyncData({params}) {
    return {
      id: params.id,
    }
  },
  created() {
    if (this.id) this.getStudent(this.id);
    else this.isLoading = false;
  },
  methods: {
    submit() {
      this.isLoading = true;
      this.$services.service.saveStudent(this.student.id, this.student.rate).then((res) => {
        this.$nuxt.$emit("success", "successfully_saved_student")
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
    getStudent(id) {
      this.$services.service.getUserById(id).then((res) => {
        this.student = res
        this.isLoading = false;
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
    disable() {
      this.$services.service.disableStudent(this.student.id).then((res) => {
        this.$nuxt.$emit("success", "successfully_disabled_student")
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
    }
  }
}
</script>

<style>
#overlay {
  max-width: 28rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
