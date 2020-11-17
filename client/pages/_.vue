<template>
  <div>
    <TutorHome v-if='user && user.tutorAccount' :user='user'></TutorHome>
    <StudentHome v-else-if='user && !user.tutorAccount' :user='user'></StudentHome>
    <Welcome v-else></Welcome>
  </div>
</template>

<script>
import TutorHome from "@/pages/TutorHome";
import StudentHome from "@/pages/StudentHome";
import Welcome from "@/pages/Welcome";

export default {
  name: "_",
  layout: "index",
  components: {Welcome, StudentHome, TutorHome},
  props: [
    'user'
  ]
}
</script>

<style scoped>

</style>