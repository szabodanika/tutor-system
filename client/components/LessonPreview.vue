<template>
  <b-card no-body id='container'>
    <b-row no-gutters align-v='center'>
      <b-col cols='12' align='middle' class='time'>
        {{ `${formatTime(lesson.start)}-${formatTime(lesson.end)} `}} ({{lesson.hours}}{{ lesson.hours > 1 ? ` hours` :
          ` hour` }})
      </b-col>
      <b-col cols='6' :class='`text-${lesson.paid?"success":"danger"}`'>
        <b-icon icon='check' v-if='lesson.paid'></b-icon>
        <b-icon icon='x' v-else></b-icon>
        {{lesson.paid?"Paid":"Not paid"}}
      </b-col>
      <b-col  cols='6' align='right'>
        <b-button-group>
        <b-button size="sm" variant='outline-primary'>
          <b-icon icon='pencil'></b-icon>
        </b-button>
        <b-button size="sm" variant='outline-danger'>
          <b-icon icon='x-circle'></b-icon>
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
      let hours = date.getHours()>12?date.getHours()-12:date.getHours()
      let minutes = date.getMinutes()
      if(minutes < 10) minutes = "0" + minutes
      let ampm = date.getHours() >= 12?"PM":"AM"
      return hours + (minutes=="00"?"":(":" + minutes)) + ampm
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
  .time{
    margin-bottom: 0.5rem;
  }
</style>