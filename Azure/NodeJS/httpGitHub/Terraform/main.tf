module "github" {
  source   = "../../../Terraform/"
  app_name = "githubazfuncnodejs"
  image_details = [{
    name         = "azfuncnodejs"
    tag          = "main"
    image_repo   = "https://ghcr.io/tpayne"
    health_probe = "/api/version"
  }]
}

output "url" {
  value       = module.github.app-url
  description = "The entry function URL"
}
