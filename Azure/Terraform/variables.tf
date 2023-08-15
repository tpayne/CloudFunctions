variable "app_name" {
  type        = string
  description = "The name of the application"
  default     = "testapp"
}

variable "location" {
  type        = string
  description = "The location of the application"
  default     = "West Europe"
}

variable "image_details" {
  type = list(object({
    name         = string
    tag          = string
    image_repo   = string
    health_probe = optional(string)
    permenant    = optional(bool, true)
    app_settings = optional(map(string))
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
}
