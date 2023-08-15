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

variable "func_settings" {
  type = list(object({
    name           = string
    java_version   = optional(string)
    node_version   = optional(string)
    python_version = optional(string)
    docker = optional(object({
      registry_url = string
      image_name   = string
      image_tag    = string
    }))
    health_probe = optional(string)
    permenant    = optional(bool, true)
    app_settings = optional(map(string))
  }))

  description = "The details of the functions to deploy"
  default = [
    {
      name = "azfuncnodejs"
      docker = {
        image_name   = "azfuncnodejs"
        image_tag    = "main"
        registry_url = "https://ghcr.io/tpayne"
      }
      health_probe = "/api/version"
    },
    {
      name = "azfuncpython"
      docker = {
        image_name   = "azfuncpython"
        image_tag    = "main"
        registry_url = "https://ghcr.io/tpayne"
      }
      health_probe = "/api/version"
    },
    {
      name         = "java"
      java_version = "17"
    },
    {
      name           = "python"
      python_version = "3.10"
    }
  ]
}
