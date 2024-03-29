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
| <a name="input_func_settings"></a> [func\_settings](#input\_func\_settings) | The details of the functions to deploy | <pre>map(object({<br>    java_version   = optional(string)<br>    node_version   = optional(string)<br>    python_version = optional(string)<br>    docker = optional(object({<br>      registry_url = string<br>      image_name   = string<br>      image_tag    = string<br>    }))<br>    health_probe = optional(string)<br>    permenant    = optional(bool, true)<br>    app_settings = optional(map(string))<br>  }))</pre> | <pre>{<br>  "azfuncnodejs": {<br>    "docker": {<br>      "image_name": "azfuncnodejs",<br>      "image_tag": "main",<br>      "registry_url": "https://ghcr.io/tpayne"<br>    },<br>    "health_probe": "/api/version"<br>  },<br>  "azfuncpython": {<br>    "docker": {<br>      "image_name": "azfuncpython",<br>      "image_tag": "main",<br>      "registry_url": "https://ghcr.io/tpayne"<br>    },<br>    "health_probe": "/api/version"<br>  },<br>  "java": {<br>    "java_version": "17"<br>  },<br>  "python": {<br>    "python_version": "3.10"<br>  }<br>}</pre> | no |
| <a name="input_location"></a> [location](#input\_location) | The location of the application | `string` | `"West Europe"` | no |

## Outputs

| Name | Description |
|------|-------------|
| <a name="output_app-url"></a> [app-url](#output\_app-url) | The URL(s) that the Azure function(s) get deployed to |
<!-- END_TF_DOCS -->
