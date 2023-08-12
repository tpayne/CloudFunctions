resource "azurerm_resource_group" "rg_name" {
  name     = "${var.app_name}-rsg"
  location = "West Europe"
}

resource "azurerm_storage_account" "storage" {
  name                     = lower("${var.app_name}sa")
  resource_group_name      = azurerm_resource_group.rg_name.name
  location                 = azurerm_resource_group.rg_name.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_service_plan" "plan" {
  name                = "${var.app_name}-service-plan"
  resource_group_name = azurerm_resource_group.rg_name.name
  location            = azurerm_resource_group.rg_name.location
  os_type             = "Linux"
  sku_name            = "P1v2"
}

resource "azurerm_linux_function_app" "funcApp" {
  name                 = "${var.app_name}-func"
  location             = azurerm_resource_group.rg_name.location
  resource_group_name  = azurerm_resource_group.rg_name.name
  storage_account_name = azurerm_storage_account.storage.name

  storage_account_access_key = azurerm_storage_account.storage.primary_access_key
  service_plan_id            = azurerm_service_plan.plan.id

  app_settings = {
    FUNCTION_APP_EDIT_MODE                   = "readOnly"
    https_only                               = false
    WEBSITES_ENABLE_APP_SERVICE_STORAGE      = false
    WEBSITE_CONTENTAZUREFILECONNECTIONSTRING = azurerm_storage_account.storage.primary_connection_string
    WEBSITE_CONTENTSHARE                     = azurerm_storage_account.storage.name
  }

  site_config {
    always_on         = true
    health_check_path = "/api/version"
    application_stack {
      docker {
        registry_url = var.image_name.image_repo
        image_name   = var.image_name.name
        image_tag    = var.image_name.tag
      }
    }
  }
}
