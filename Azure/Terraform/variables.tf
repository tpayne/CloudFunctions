variable "app_name" {
  type        = string
  description = "The name of the application"
  default     = "testapp"
}

variable "image_name" {
  type = object({
    name       = string
    tag        = string
    image_repo = string
  })

  description = "The details of the functions image"
  default = {
    name       = "azfuncnodejs"
    tag        = "main"
    image_repo = "https://ghcr.io/tpayne"
  }

  nullable = false
}
