<template>
  <div class="container" v-if='user'>
    <b-row>
      <b-col>
      </b-col>
    </b-row>
  </div>
</template>

<script>

import WeekView from "@/components/WeekView";

export default {
  components: {WeekView},
  layout: 'index',
  name: 'Home',
  props: [
    'user'
  ],
  data() {
    return {
    }
  },
  created() {
  },
  methods: {
  }
}
</script>

<style>
</style>
