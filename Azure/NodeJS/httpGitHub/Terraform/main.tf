module "github" {
  source   = "../../../Terraform/"
  app_name = "githubazfuncnodejs"
  func_settings = {
    "azfuncnodejs" = {
      docker = {
        image_name   = "azfuncnodejs"
        image_tag    = "main"
        registry_url = "https://ghcr.io/tpayne"
      }
      health_probe = "/api/version"
    }
  }
}

output "url" {
  value       = module.github.app-url
  description = "The entry function URL"
}
