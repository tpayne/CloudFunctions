module "github" {
  source   = "../../../Terraform/"
  app_name = "githubazfuncpythonjs"
  image_name = {
    name       = "azfuncpython"
    tag        = "main"
    image_repo = "https://ghcr.io/tpayne"
  }
}

output "url" {
  value       = module.github.app-url
  description = "The entry function URL"
}