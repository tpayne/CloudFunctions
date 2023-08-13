output "app-url" {
  value = [
    for k, v in azurerm_linux_function_app.funcApp : lower("https://${v.name}.azurewebsites.net/")
  ]
  description = "The URL(s) that the Azure function gets deployed to"
}
