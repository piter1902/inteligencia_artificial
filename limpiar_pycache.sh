#!/bin/bash

# Para limpiar los directorios __pycache__

find . -regex __pycache__ -type d -exec rm -rf {} \;
