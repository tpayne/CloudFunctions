locals {
  storage-details = {
    account_tier = "Standard"
    replication  = "LRS"
  }
  app-plan = {
    os_type = "Linux"
    sku     = "P1v2"
  }
}
