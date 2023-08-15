module "github" {
  source   = "../../../Terraform/"
  app_name = "githubazfuncpythonjs"
  func_settings = [{
    name = "azfuncpython"
    docker = {
      image_name   = "azfuncpython"
      image_tag    = "main"
      registry_url = "https://ghcr.io/tpayne"
    }
    health_probe = "/api/version"
  }]
}

output "url" {
  value       = module.github.app-url
  description = "The entry function URL"
}
