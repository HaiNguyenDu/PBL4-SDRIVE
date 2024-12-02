@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text" /remove "PBL4\Phuc"
icacls "\\192.168.1.25\SDriver\XPhuc\text" /remove "PBL4\Phuc" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\Phuc":(OI)(CI)M
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\Phuc:(OI)(CI)M" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\text" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\text\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
