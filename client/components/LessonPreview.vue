<template>
  <b-card no-body id='container'>
    <b-row no-gutters>
      <b-col>
        <b-checkbox>
          Paid
        </b-checkbox>
      </b-col>
      <b-col>
        {{ lesson.hours }} {{ lesson.hours > 1 ? `hours` : `hour` }}
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
  }
}
</script>

<style scoped>
  #container {
    padding: 0.5rem;
    margin: 0;
  }
</style>