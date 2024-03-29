output "app-url" {
  value = {
    for v in azurerm_linux_function_app.funcApp : v.name => lower("https://${v.name}.azurewebsites.net/")
  }
  description = "The URL(s) that the Azure function(s) get deployed to"
}
