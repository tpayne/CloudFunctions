output "app-url" {
  value = lower("https://${azurerm_linux_function_app.funcApp.name}.azurewebsites.net/")
  description = "The URL that the Azure function gets deployed to"
}