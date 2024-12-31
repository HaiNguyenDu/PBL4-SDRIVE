@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /grant "PBL4\XPhuc:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /grant "PBL4\XPhuc:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /remove "Everyone"
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /inheritance:d
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\XPhuc\hi.txt" /grant "PBL4\XPhuc:F"
