@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "PBL4\XPhuc:F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "PBL4\XPhuc:F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /remove "Everyone"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "Everyone":R
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /inheritance:d
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\Test file.txt" /grant "PBL4\XPhuc:F"
