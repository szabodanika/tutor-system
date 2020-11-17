<template>
  <b-card no-body id='container'>
    <b-row no-gutters align-v='center'>
      <b-col cols='12' align='middle' class='time'>
        {{ `${formatTime(lesson.start)}-${formatTime(lesson.end)} ` }} ({{ lesson.hours }}{{
          lesson.hours > 1 ? ` hours` :
              ` hour`
        }})
      </b-col>
      <b-col cols='6' :class='`text-${lesson.paid?"success":"danger"}`'>
        <b-icon icon='check' v-if='lesson.paid'></b-icon>
        <b-icon icon='x' v-else></b-icon>
        {{ lesson.paid ? "Paid" : "Not paid" }}
      </b-col>
      <b-col cols='6' align='right'>
        <b-button-group>
          <b-button v-if='!lesson.locked'  size="sm" variant='outline-primary' @click='editLesson()'>
            <b-icon icon='pencil'></b-icon>
          </b-button>
          <b-button size="sm" :variant='`${!lesson.locked?"outline-":""}primary`' @click='switchLessonLock()'>
            <b-icon v-if='!lesson.locked' icon='unlock'></b-icon>
            <b-icon v-else icon='lock'></b-icon>
          </b-button>
        </b-button-group>
      </b-col>
    </b-row>
  </b-card>
</template>

<script>
export default {
  name: "LessonPreview",
  props: {
    lesson: {
      hours: 0,
      payment: null
    }
  },
  data() {
    return {}
  },
  methods: {
    formatTime(date) {
      let hours = date.getHours() > 12 ? date.getHours() - 12 : date.getHours()
      let minutes = date.getMinutes()
      if (minutes < 10) minutes = "0" + minutes
      let ampm = date.getHours() >= 12 ? "PM" : "AM"
      return hours + (minutes == "00" ? "" : (":" + minutes)) + ampm
    },
    switchLessonLock(){
      if(this.lesson.locked){
        this.$services.service.unlockLesson(this.lesson.id).then((res) => {
          this.$nuxt.$emit("success", "successfully_unlocked_lesson")
          this.lesson.locked = false
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
        this.$services.service.lockLesson(this.lesson.id).then((res) => {
          this.$nuxt.$emit("success", "successfully_locked_lesson")
          this.lesson.locked = true
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
    editLesson(){
      this.$nuxt.$router.push(`lesson/${this.lesson.id}`)
    }
  }
}
</script>

<style scoped>
#container {
  width: 14rem;
  padding: 0.5rem;
  margin: 0;
}

.time {
  margin-bottom: 0.5rem;
}
</style>