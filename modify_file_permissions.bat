@echo off
icacls "\\192.168.10.25\SDriver\Thanhan\hello.txt" /inheritance:r
icacls "\\192.168.10.25\SDriver\Thanhan\hello.txt" /remove "PBL4\XPhuc"
icacls "\\192.168.10.25\SDriver\Thanhan\hello.txt" /grant "PBL4\XPhuc":M
icacls "\\192.168.10.25\SDriver\Thanhan\hello.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\Thanhan\hello.txt" /grant "PBL4\Thanhan:F"
