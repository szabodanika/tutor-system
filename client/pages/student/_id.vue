<template>
  <b-overlay :show="isLoading" rounded="sm" :rounded='true' variant='transparent'
             spinner-type='grow'  :opacity="1.0"  blur="1rem" id='overlay'>
    <b-card id='new-lesson-card' align='left'>
      {{student}}
      <template #header>
        Edit Student
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
      <b-form-group label='Location' v-if='!lesson.location || lesson.location != "Custom"'>
        <b-form-select v-model="lesson.location"
                       :options="locations"></b-form-select>
      </b-form-group>
      <b-form-group v-if='lesson.location && lesson.location == "Custom"' label='Location'>
        <b-form-input v-model="lesson.customLocation"></b-form-input>
      </b-form-group>
      <p v-if='lesson.location && lesson.start && lesson.end && lesson.date'>
        {{ lesson.location }} lesson from {{ lesson.start }} to {{ lesson.end }} on {{ lesson.date }}
        with
        {{ student.firstName }}
      </p>
      <b-button block @click='submit' variant="primary">Save</b-button>
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
      isLoading: true
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
