@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\Tea" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\Tea\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\Tea" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\Tea" /remove "Everyone"
icacls "\\192.168.1.25\SDriver\XPhuc\Tea" /remove "Everyone" /T
