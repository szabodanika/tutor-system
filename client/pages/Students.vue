<template>
  <b-overlay :show='isLoading' v-if='user' id="container" :rounded='true'
             spinner-type='grow' variant='transparent'  :opacity="1.0"  blur="1rem">
    <b-row align='middle' id='tutorCode'>
      <b-col>
        <b-card>
          Your tutor code is
          <h1>
            {{ pad(user.tutorCode, 6) }}
          </h1>
          <b-button variant='outline-primary' @click='resetTutorCode'>reset</b-button>
        </b-card>
      </b-col>
    </b-row>
    <b-row align='middle'>
      <b-col>
        <b-button pill @click='$nuxt.$router.push("/newstudent")'>
          <b-icon icon='plus-circle'></b-icon>
          Add student</b-button>
      </b-col>
    </b-row>
    <br>
    <b-row>
      <b-col align='middle' v-if='students && students.length == 0'>
        <p>
          You don't have any students yet
        </p>
      </b-col>
      <b-col xl='6' md='6' sm='12' v-for='(student, index) in students' v-bind:key='index'>
        <StudentPreview class='student' :student='student.id'></StudentPreview>
      </b-col>
    </b-row>

  </b-overlay>
</template>

<script>

import WeekView from "@/components/WeekView";
import StudentPreview from "@/components/StudentPreview";

export default {
  components: {StudentPreview, WeekView},
  layout: 'index',
  name: 'Students',
  props: [
    'user',
  ],
  data() {
    return {
      students: null,
      isLoading: true
    }
  },
  mounted() {
    this.getStudents()
  },
  methods: {
    getStudents() {
      this.isLoading = true
      this.$services.service.getStudents(this.user.id).then((res) => {
        this.students = res
        this.isLoading = false
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
    resetTutorCode() {
      this.$services.service.resetTutorCode(this.user.id).then((res) => {
        this.user.tutorCode = res
        this.$nuxt.$emit("success", "Successfully reset tutor code")
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
    pad(num, size) {
      num = num.toString();
      while (num.length < size) num = "0" + num;
      return num;
    }
  }
}
</script>

<style>
#tutorCode {
  max-width: 20rem;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 1rem;
}

#container {
  /*height: 100em;*/
}

.student{
  margin: 0.5rem;
}

</style>
