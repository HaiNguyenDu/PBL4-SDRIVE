@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /remove "PBL4\Thanhan"
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /remove "PBL4\Thanhan" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\Thanhan":(OI)(CI)M
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\Thanhan:(OI)(CI)M" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\folder" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\folder\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
