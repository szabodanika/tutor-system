<template>
  <b-overlay :show="isLoading" rounded="sm" :rounded='true' variant='transparent'
             spinner-type='grow'  :opacity="1.0"  blur="1rem" id='overlay'>
    <b-card id='new-lesson-card' align='left'>
      <template #header>
        Edit lesson
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
      <b-form-group label='Location'>
        <b-form-select v-model="lesson.location"
                       :options="locations"></b-form-select>
      </b-form-group>
      <b-form-group v-if='lesson.location && lesson.location == "Custom"'>
        <b-form-input v-model="lesson.customLocation"></b-form-input>
      </b-form-group>
      <p v-if='lesson.location && lesson.start && lesson.end && lesson.date'>
        {{ lesson.location }} lesson from {{ lesson.start }} to {{ lesson.end }} on {{ lesson.date }}
        with
        {{ student.firstName }}
      </p>
      <b-button block @click='submit' variant="primary">Save</b-button>
      <br>
      <b-row align-v='center'>
        <b-col cols='8'>
          <b-form-checkbox button button-variant='outline-primary' v-model='deleteEnabled'>
            Delete lesson
          </b-form-checkbox>
        </b-col>
        <b-col align='left'>
          <b-button :disabled='!deleteEnabled' block
                    @click='deleteLesson'
                    variant="danger">
            Delete
          </b-button>
        </b-col>
      </b-row>
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
      lesson: {},
      student: {},
      students: [],
      locations: ["Discord", "Microsoft Teams", "Zoom", "Face to face", "Custom"],
      allweeks: [],
      isLoading: true,
      deleteEnabled: false
    }
  },
  watch: {
    id: function (val) {
      if (val) this.getLesson()
    }
  },
  asyncData({params}) {
    return {
      id: params.id,
    }
  },
  created() {
    if(this.id) this.getLesson(this.id);
    else this.isLoading = false;
    this.user.students.forEach((student) => {
      this.students.push({value: student, text: `${student.firstName} ${student.lastName ? student.lastName : ""}`},)
    })
  },
  methods: {
    submit() {
      let location = this.lesson.location == "Custom" ? this.lesson.customLocation : this.lesson.location
      this.$services.service.saveLesson(this.lesson.id, this.student.id, this.lesson.start, this.lesson.end,
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
    getLesson(id) {
        this.$services.service.getLessonById(id).then((res) => {
          this.lesson = res
          this.lesson.date =
              `${this.lesson.start.getFullYear()}-${this.lesson.start.getMonth()+1}-${this.lesson.start.getDate()}`
          this.lesson.start =
              `${this.lesson.start.getHours()}:${this.lesson.start.getMinutes()}:${this.lesson.start.getSeconds()}`
          this.lesson.end = `${this.lesson.end.getHours()}:${this.lesson.end.getMinutes()}:${this.lesson.end.getSeconds()}`
          this.student = this.lesson.student
          this.lesson.student = this.lesson.student.id
          if (!this.locations.includes(this.lesson.location)) {
            this.lesson.customLocation = this.lesson.location
            this.lesson.location = "Custom"
          }
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
    deleteLesson() {
      this.$services.service.deleteLesson(this.lesson.id).then((res) => {
        this.$nuxt.$emit("success", "successfully_deleted_lesson")
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
