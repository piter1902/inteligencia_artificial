#!/bin/bash

# Para limpiar los directorios __pycache__

find . -maxdepth 5  -regex __pycache__ -type d -exec rm -rf {} \;
