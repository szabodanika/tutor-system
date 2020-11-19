<template>
  <div>
    <b-card id='container' v-if='user'>
      <template #header>
        <b-row no-gutters align-v='center'>
          <b-col align='left'>
            <div class="d-flex align-items-center">
              <b-avatar
                  :text="initials"
                  variant="primary"></b-avatar>
              <span style='margin-left: 1rem'>{{ user.firstName }} {{ user.lastName }}</span>
            </div>
          </b-col>
          <b-col v-if='user.disabled' cols='3' align='right'>
            <h6 class='text-muted'>Disabled
              <b-icon icon='lock'></b-icon>
            </h6>
          </b-col>
          <b-col cols='2' align='right'>
            <b-button variant='outline-primary' @click='editAccount'>
              <b-icon icon='pencil'></b-icon>
            </b-button>
          </b-col>
        </b-row>
      </template>
      <b-col cols='12'>
        <b-row>
          <b-col cols='5'>
            <b-row>
              Email
            </b-row>
            <b-row>
              Phone
            </b-row>
            <b-row>
              Join date
            </b-row>
          </b-col>
          <b-col>
            <b-row>
              {{ user.email ? user.email : "Not added yet" }}
            </b-row>
            <b-row>
              {{ user.phone ? user.phone : "Not added yet" }}
            </b-row>
            <b-row>
              {{ formatDate(user.registered) }}
            </b-row>
          </b-col>
        </b-row>
      </b-col>

      <br>
      <h4>
        Lessons
      </h4>

      <b-col cols='12'>
        <b-row>
          <b-col cols='5'>
            <b-row>
              Lessons so far
            </b-row>
            <b-row>
              Hours so far
            </b-row>
            <b-row>
              Unpaid
            </b-row>
            <b-row>
              Total paid
            </b-row>
            <b-row>
              Rate
            </b-row>
          </b-col>
          <b-col>
            <b-row>
              {{ user.studentLessons.length }}
            </b-row>
            <b-row>
              {{ user.totalHours }} hour{{ user.totalHours < 2 ? '' : 's' }}
            </b-row>
            <b-row>
              £{{ user.totalDebt }}
            </b-row>
            <b-row>
              £{{ user.totalPaid }}
            </b-row>
            <b-row>
              £{{ user.rate }} per hour
            </b-row>
          </b-col>
        </b-row>
      </b-col>
    </b-card>
  </div>
</template>

<script>
export default {
  name: "StudentPreview",
  props: [
    'user',
  ],
  data() {
    return {
      initials: ""
    }
  },
  created() {
    if (this.user.firstName) this.initials += this.user.firstName.substr(0, 1)
    if (this.user.lastName) this.initials += this.user.lastName.substr(0, 1)
  },
  methods: {
    formatDate(date) {
      try {
        return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
      } catch (e) {
        this.$nuxt.$emit("error")
      }
    },
    formatDateTime(date) {
      try {
        return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +
            date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
      } catch (e) {
        this.$nuxt.$emit("error")
      }
    },
    editAccount(){
      this.$nuxt.$router.push('/account')
    }
  }
}
</script>

<style scoped>
#container {
  /*padding: 0.5rem;*/
  /*min-width: 200px;*/
  font-weight: 400;
  /*max-height: 20rem;*/
}
</style>