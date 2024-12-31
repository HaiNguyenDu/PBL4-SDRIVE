@echo off
icacls "\\192.168.1.25\SDriver\XPhuc\newifs" /inheritance:r
icacls "\\192.168.1.25\SDriver\XPhuc\newifs" /remove "PBL4\Thanhan"
icacls "\\192.168.1.25\SDriver\XPhuc\newifs" /remove "PBL4\Thanhan" /T
icacls "\\192.168.1.25\SDriver\XPhuc\newifs" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\newifs" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\XPhuc\newifs\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\XPhuc\newifs\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
