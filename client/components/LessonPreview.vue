<template>
  <b-card>
    <b-card-title>
      Week 12
    </b-card-title>
    <b-table striped :fields='fields' :items='lessons'>
      <template #cell(Day)="data">
        {{ days[data.index] }}
      </template>
    </b-table>
  </b-card>
</template>

<script>
export default {
  name: "WeekView",
  data() {
    return {
      days: [
        'Monday',
        'Tuesday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday',
        'Sunday'
      ],
      fields: [
        'Day',
        {
          key: '0',
          label: 'Test User 1',
        },
        {
          key: '1',
          label: 'Test User 1',
        }
      ],
      lessons: [
        {0: 'Test Lesson', 1: 'Test Lesson'},
        {1: 'Test Lesson'},
        {0: 'Test Lesson', 1: 'Test Lesson'},
        {1: 'Test Lesson'},
        {0: 'Test Lesson'},
      ]
    }
  }
}
</script>

<style scoped>

</style>