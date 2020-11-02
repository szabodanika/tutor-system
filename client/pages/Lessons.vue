<template>
  <div class="container">
    <WeekView/>
    <br><WeekView/>
    <br>

    <WeekView/>
  </div>
</template>

<script>

import WeekView from "@/components/WeekView";

export default {
  components: {WeekView},
  layout: 'Main'
}
</script>

<style>
</style>
