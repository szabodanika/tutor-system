<template>
  <div class="container" v-if='user'>
    <h2>
      Welcome back, {{user.firstName}} ❤️
    </h2>
    <br>
    <br>
    <b-row>
      <b-col xl='6' md='6' sm='12'>
        <StudentSelf id='self' :user='user' ></StudentSelf>
      </b-col>
      <b-col xl='6' md='6' sm='12'>
        <TutorPreview id='tutor' :user='user' :tutor='user.tutor'></TutorPreview>
      </b-col>
      <b-col cols='12'>
        <LessonList id='lessons' :user='user' ></LessonList>
      </b-col>
      <b-col xl='6' md='6' sm='12'>
        <b-card>
          <template #header>
            <b-row align-v='center'>
              <b-col align='left'>
                <h2 class='text-primary'> Your Payments </h2>
              </b-col>
            </b-row>
          </template>
          <PaymentPreview v-for='(payment, index) in user.paymentsSent' v-bind:key='index'
                          :payment='payment' :owner="payment.tutor"></PaymentPreview>
        </b-card>
      </b-col>
    </b-row>
    <br>
  </div>
</template>

<script>
import TutorPreview from "@/components/TutorPreview";
import LessonList from "@/components/LessonList";
import PaymentPreview from "@/components/PaymentPreview";
export default {
  components: {PaymentPreview, LessonList, TutorPreview},
  layout: 'index',
  name: 'StudentHome',
  props: [
    'user'
  ],
  data() {
    return {
      week: this.getWeekNumber(new Date())
    }
  },
  created() {
    if (!this.user)  {
      this.$nuxt.$emit("error", "not_logged_in")
      return
    }
  },
  methods: {
    getWeekNumber(date) {
      let dayn = (date.getDay() + 6) % 7;
      date.setDate(date.getDate() - dayn + 3);
      let firstThursday = date.valueOf();
      date.setMonth(0, 1);
      if (date.getDay() !== 4) {
        date.setMonth(0, 1 + ((4 - date.getDay()) + 7) % 7);
      }
      return 1 + Math.ceil((firstThursday - date) / 604800000);
    },
  }
}
</script>

<style>
#tutor, #self, #lessons {
  margin-bottom: 3rem;
}
</style>
