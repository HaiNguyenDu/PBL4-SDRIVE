@echo off
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\XPhuc:F"
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /inheritance:r
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\XPhuc:F"
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /remove "PBL4\Phuc"
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\Phuc":M
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\XPhuc\doccom.docx" /grant "PBL4\XPhuc:F"
