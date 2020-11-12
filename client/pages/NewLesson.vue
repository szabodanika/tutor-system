<template>
  <b-overlay>
    <div class="container" align='middle'>
      <b-col>
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
          <b-form-group label='Location'  v-if='!lesson.location || lesson.location != "Custom"' >
            <b-form-select  v-model="lesson.location"
                            :options="locations"></b-form-select>
          </b-form-group>
          <b-form-group v-if='lesson.location && lesson.location == "Custom"' label='Location'>
            <b-form-input v-model="lesson.customlocation"></b-form-input>
          </b-form-group>
          <p v-if='lesson.location && startTime && endTime && date'>
            {{lesson.location}} lesson from {{startTime.formatted}} to {{endTime.formatted}} on {{date.activeYMD}} with
            {{student.firstName}}
          </p>
          <b-button block @click='submit' variant="primary">Save</b-button>
        </b-card>
      </b-col>
    </div>
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
    }
  },
  created() {
    if (!this.user)  {
      this.$nuxt.$emit("error", "not_logged_in")
      return
    }
    this.user.students.forEach((student) => {
      this.students.push({value: student, text: `${student.firstName} ${student.lastName}`},)
    })
  },
  methods: {
    submit() {
      this.$services.service.savePayment(null, this.user.id, this.payment.student, this.payment.amount,
          this.payment.cash,
          this.payment.transactionNumber).then((res) => {
        this.$nuxt.$emit("success", "succesfully_saved_payment")
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
    onStartTimeUpdate(context){
      this.startTime = context
    },
    onEndTimeUpdate(context){
      this.endTime = context
    },
    onDateUpdate(context){
      this.date = context
    }
  }
}
</script>

<style>
#new-lesson-card {
  max-width: 28rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
