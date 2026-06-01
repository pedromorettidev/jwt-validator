terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# =========================
# ECR - Docker Registry
# =========================
resource "aws_ecr_repository" "jwt_validator" {
  name = var.ecr_repository_name

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Project = "jwt-validator"
    ManagedBy = "terraform"
  }
}