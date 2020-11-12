import Service from '~/services/service'

export default ({ app }, inject) => {
  let baseUrl = "http://localhost:8080/api"

  app.$axios.defaults.withCredentials = true
	const services = {
		service: new Service(app.$axios, baseUrl),
	}

	inject('services', services)
}
