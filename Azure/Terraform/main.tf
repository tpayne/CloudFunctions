/*
 * This module is used to install function apps into Azure
 * based on containers
 */

resource "azurerm_resource_group" "rg_name" {
  name     = "${var.app_name}-rsg"
  location = "West Europe"
}

resource "azurerm_storage_account" "storage" {
  name                     = lower("${var.app_name}sa")
  resource_group_name      = azurerm_resource_group.rg_name.name
  location                 = azurerm_resource_group.rg_name.location
  account_tier             = local.storage-details.account_tier
  account_replication_type = local.storage-details.replication
}

resource "azurerm_service_plan" "plan" {
  name                = "${var.app_name}-service-plan"
  resource_group_name = azurerm_resource_group.rg_name.name
  location            = azurerm_resource_group.rg_name.location
  os_type             = local.app-plan.os_type
  sku_name            = local.app-plan.sku
}

resource "azurerm_linux_function_app" "funcApp" {
  for_each             = { for i, v in var.image_name : i => v if length(v.name) > 0 }
  name                 = "${var.app_name}-func-${each.value.name}"
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
    always_on         = each.value.permenant
    health_check_path = each.value.health_probe
    application_stack {
      docker {
        registry_url = each.value.image_repo
        image_name   = each.value.name
        image_tag    = each.value.tag
      }
    }
  }
}
