locals {
  storage-details = {
    account_tier = "Standard"
    replication  = "LRS"
  }
  app-plan = {
    os_type = "Linux"
    sku     = "P1v2"
  }
  app-settings = {
    FUNCTION_APP_EDIT_MODE              = "readOnly"
    https_only                          = false
    WEBSITES_ENABLE_APP_SERVICE_STORAGE = false
  }
}
