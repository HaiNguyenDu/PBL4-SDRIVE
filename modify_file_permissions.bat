@echo off
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\Thanhan:F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /inheritance:r
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\Thanhan:F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /remove "PBL4\XPhuc"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\XPhuc":M
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /inheritance:d
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\Administrator:F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\folder\file.txt" /grant "PBL4\Thanhan:F"
