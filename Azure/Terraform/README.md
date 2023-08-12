# Terraform for Deploying Functions

This Terraform module is used to deploy Azure Function Container apps (with supporting infrastructure) to an Azure CSP.

It is used by the samples provided in the above directories.

<!-- BEGIN_TF_DOCS -->
This module is used to install function apps into Azure
based on containers

## Requirements

| Name | Version |
|------|---------|
| <a name="requirement_terraform"></a> [terraform](#requirement\_terraform) | >= 0.14.0 |
| <a name="requirement_azurerm"></a> [azurerm](#requirement\_azurerm) | >= 2.26 |

## Providers

| Name | Version |
|------|---------|
| <a name="provider_azurerm"></a> [azurerm](#provider\_azurerm) | 3.69.0 |

## Modules

No modules.

## Resources

| Name | Type |
|------|------|
| [azurerm_linux_function_app.funcApp](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/linux_function_app) | resource |
| [azurerm_resource_group.rg_name](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/resource_group) | resource |
| [azurerm_service_plan.plan](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/service_plan) | resource |
| [azurerm_storage_account.storage](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/storage_account) | resource |

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| <a name="input_app_name"></a> [app\_name](#input\_app\_name) | The name of the application | `string` | `"testapp"` | no |
| <a name="input_health_probe"></a> [health\_probe](#input\_health\_probe) | Health probe path | `string` | `"/api/version"` | no |
| <a name="input_image_name"></a> [image\_name](#input\_image\_name) | The details of the functions image | <pre>object({<br>    name       = string<br>    tag        = string<br>    image_repo = string<br>  })</pre> | <pre>{<br>  "image_repo": "https://ghcr.io/tpayne",<br>  "name": "azfuncnodejs",<br>  "tag": "main"<br>}</pre> | no |

## Outputs

| Name | Description |
|------|-------------|
| <a name="output_app-url"></a> [app-url](#output\_app-url) | The URL that the Azure function gets deployed to |
<!-- END_TF_DOCS -->