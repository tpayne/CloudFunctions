module "github" {
  source   = "../../../Terraform/"
  app_name = "githubazfuncnodejs"
  image_name = {
    name       = "azfuncnodejs"
    tag        = "main"
    image_repo = "https://ghcr.io/tpayne"
  }
}

output "url" {
  value       = module.github.app-url
  description = "The entry function URL"
}