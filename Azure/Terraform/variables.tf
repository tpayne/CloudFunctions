variable "app_name" {
  type        = string
  description = "The name of the application"
  default     = "testapp"
}

variable "image_name" {
  type = list(object({
    name         = string
    tag          = string
    image_repo   = string
    health_probe = string
  }))

  description = "The details of the functions to deploy"
  default = [
    {
      name         = "azfuncnodejs"
      tag          = "main"
      image_repo   = "https://ghcr.io/tpayne"
      health_probe = "/api/version"
    },
    {
      name         = "azfuncpython"
      tag          = "main"
      image_repo   = "https://ghcr.io/tpayne"
      health_probe = "/api/version"
    }
  ]

  nullable = false
}
