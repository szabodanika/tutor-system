<template>
  <b-overlay class="container" align='middle'>
    <b-card id='new-lesson-card' align='left'>
      <template #header>
        Add new lesson
      </template>
      <b-form-group label='Select student'>
        <b-form-select v-model="student" :options="students"></b-form-select>
      </b-form-group>
      <label>Choose a date</label>
      <b-form-datepicker v-model="lesson.date" class="mb-2" @context="onDateUpdate"></b-form-datepicker>
      <b-form-group>
        <b-row>
          <b-col>
            <label>Start</label>
            <b-time v-model="lesson.start" locale="en" @context="onStartTimeUpdate"></b-time>
          </b-col>
          <b-col>
            <label>End</label>
            <b-time v-model="lesson.end" locale="en" @context="onEndTimeUpdate"></b-time>
          </b-col>
        </b-row>
      </b-form-group>
      <b-form-group label='Location' >
        <b-form-select v-model="lesson.location"
                       :options="locations"></b-form-select>
      </b-form-group>
      <b-form-group v-if='lesson.location && lesson.location == "Custom"'>
        <b-form-input v-model="lesson.customlocation" placeholder='Enter custom location'></b-form-input>
      </b-form-group>
      <p align='middle' v-if='student && lesson.location && startTime && endTime && lesson.date'>
        {{ lesson.location }} lesson <br>
        from {{ startTime.formatted }} to {{ endTime.formatted }} <br>
        on {{date.activeYMD }}<br>
        with {{ student.firstName }}
      </p>
      <b-button block @click='submit' variant="primary">Save</b-button>
    </b-card>
  </b-overlay>
</template>

<script>
export default {
  components: {},
  layout: 'index',
  name: 'NewPayment',
  props: [
    'user'
  ],
  data() {
    return {
      lesson: {},
      student: {},
      students: [],
      locations: ["Discord", "Microsoft Teams", "Zoom", "Face to face", "Custom"],
      allweeks: [],
      startTime: null,
      endTime: null,
      date: null
    }
  },
  asyncData({params, app}) {
    return {
      week: params.week,
      id: params.id
    }
  },
  created() {
    this.user.students.forEach((student) => {
      if (!student.disabled) {
        if(student.id == this.id){
          this.student = student
        }
        this.lesson.start = `12:00:00`
        this.lesson.end = `13:00:00`
        this.students.push({value: student, text: `${student.firstName} ${student.lastName ? student.lastName : ""}`})
      }
    })
    this.student = this.students[0].value
  },
  methods: {
    submit() {
      let location = this.lesson.location == "Custom" ? this.lesson.customlocation : this.lesson.location
      this.$services.service.saveLesson(-1, this.student.id, this.startTime.value, this.endTime.value,
          this.lesson.date,
          location).then((res) => {
        this.$nuxt.$emit("success", "successfully_saved_lesson")
        this.$nuxt.$router.push("/lessons")
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
    onStartTimeUpdate(context) {
      this.startTime = context
    },
    onEndTimeUpdate(context) {
      this.endTime = context
    },
    onDateUpdate(context) {
      this.date = context
    }
  }
}
</script>

<style>
#new-lesson-card {
  max-width: 28rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
