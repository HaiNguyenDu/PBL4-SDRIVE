@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\XPhuc:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\XPhuc:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /remove "PBL4\Thanhan"
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\Thanhan":M
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /inheritance:d
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hello.txt" /grant "PBL4\XPhuc:F"
