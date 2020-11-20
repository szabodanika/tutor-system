<template>
  <b-card no-body id='container' :class='lesson.paid?`paid`:`unpaid`'>
    <b-row no-gutters align-v='tp['>
      <b-col cols='12' align='middle' class='time'>
        {{ `${formatTime(lesson.start)}-${formatTime(lesson.end)} ` }}
        <br>
        {{ lesson.hours }}{{
          lesson.hours > 1 ? ` hours` :
              ` hour`
        }}
      </b-col>
      <b-col cols='12' align='center' id='editButtons'>
        <b-button-group>
          <b-button v-if='!lesson.locked' size="sm" variant='outline-primary' @click='editLesson()'>
            <b-icon icon='pencil'></b-icon>
          </b-button>
          <b-button size="sm" :variant='`${!lesson.locked?"outline-":""}primary`' @click='switchLessonLock()'>
            <b-icon v-if='!lesson.locked' icon='unlock'></b-icon>
            <b-icon v-else icon='lock'></b-icon>
          </b-button>
        </b-button-group>
        <b-checkbox :id='`info-${lesson.id}`' button size='sm' v-model='showInfo' pill
                    button-variant='outline-primary'>
          <b-icon
              icon='info'></b-icon>
        </b-checkbox>
        <b-popover
            custom-class='lesson-info-popover'
            :target='`info-${lesson.id}`'
            :show.sync="showInfo"
            placement="bottom"
            ref="popover"
        >
          <template #title>
            Lesson with {{ lesson.student.firstName }}
          </template>
          <b-row align-v='center'>
            <b-col align='middle' class='time'>
              {{ formatDate(lesson.start) }}
              {{ `${formatTime(lesson.start)}-${formatTime(lesson.end)} ` }}
              <br>
              {{ lesson.hours }}{{
                lesson.hours > 1 ? ` hours` :
                    ` hour`
              }}
              on {{ lesson.location }}
              <br>
              <p v-if='lesson.paid'>
                Paid
              </p>
              <p v-else>
                Not paid yet
              </p>
              <p>
                {{lesson.comment}}
              </p>
            </b-col>
          </b-row>
        </b-popover>
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
    return {
      showInfo: false,
    }
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
    editLesson() {
      this.$nuxt.$router.push(`lesson/${this.lesson.id}`)
    },
    formatDate(date) {
      try {
        return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
      } catch (e) {
        this.$nuxt.$emit("error")
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "assets/css/light";

#container {
  width: 10rem;
  padding: 0.5rem;
  margin: 0;
  overflow: hidden;
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

.lesson-info-popover {
  min-width: 18rem;
}
</style>