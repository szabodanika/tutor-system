<template>
  <b-card no-body id='container' :class='lesson.paid?`paid`:`unpaid`'>
    <b-row no-gutters align-v='center'>
      <b-col cols='8' align='middle' class='time'>
        {{ formatDate(lesson.start) }}
        {{ `${formatTime(lesson.start)}-${formatTime(lesson.end)} ` }}
        <br>
        {{ lesson.hours }}{{
          lesson.hours > 1 ? ` hours` :
              ` hour`
        }}
        on {{lesson.location}}
      </b-col>
      <b-col cols='4' align='center' id='editButtons'>
        <b-icon variant='primary' v-if='!lesson.locked' icon='unlock'></b-icon>
        <b-icon variant='primary' v-else icon='lock'></b-icon>
      </b-col>
    </b-row>
  </b-card>
</template>

<script>

export default {
  name: "LargeLessonPreview",
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
    switchLessonLock() {
      if (this.lesson.locked) {
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
    formatDate(date) {
      try {
        return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
      } catch (e) {
        this.$nuxt.$emit("error")
      }
    },
  }
}
</script>

<style lang="scss" scoped>
@import "assets/css/light";

#container {
  padding: 0.5rem;
  margin: 0.5rem;
  min-height: 4rem;
}

.time {
}

.paid {
  border-left: $success solid 3px !important;
  border-bottom-left-radius: 0;
  border-top-left-radius: 0;
}

.unpaid {
  border-left: $danger solid 3px !important;
  border-bottom-left-radius: 0;
  border-top-left-radius: 0;
}
</style>